const express = require('express');
const cors = require('cors');
const net = require('node:net');

const BRIDGE_PORT = Number(process.env.BRIDGE_PORT || 8090);
const TCP_SERVER_HOST = process.env.TCP_SERVER_HOST || 'server';
const TCP_SERVER_PORT = Number(process.env.TCP_SERVER_PORT || 8080);
const TCP_TIMEOUT_MS = Number(process.env.TCP_TIMEOUT_MS || 12000);

const app = express();
app.use(cors());
app.use(express.json({ limit: '1mb' }));

function sendCommandTcp(command, expectEnd = false) {
  return new Promise((resolve, reject) => {
    let settled = false;
    let buffer = '';

    const client = net.createConnection(
      {
        host: TCP_SERVER_HOST,
        port: TCP_SERVER_PORT,
      },
      () => {
        client.write(`${command}\n`);
      }
    );

    const cleanup = () => {
      client.removeAllListeners();
      client.end();
      client.destroy();
    };

    const finish = (value, isError = false) => {
      if (settled) {
        return;
      }
      settled = true;
      cleanup();
      if (isError) {
        reject(value);
      } else {
        resolve(value);
      }
    };

    client.setTimeout(TCP_TIMEOUT_MS, () => {
      finish(new Error('Timeout verso server TCP'), true);
    });

    client.on('data', (chunk) => {
      buffer += chunk.toString('utf8');

      if (expectEnd) {
        if (buffer.includes('\nEND\n') || buffer.endsWith('END\n') || buffer.trimEnd().endsWith('END')) {
          finish(buffer.trim());
        }
        return;
      }

      const idx = buffer.indexOf('\n');
      if (idx !== -1) {
        const line = buffer.slice(0, idx).trim();
        finish(line);
      }
    });

    client.on('error', (err) => {
      finish(err, true);
    });

    client.on('end', () => {
      if (!settled) {
        finish(buffer.trim());
      }
    });
  });
}

app.get('/health', (_req, res) => {
  res.json({ ok: true, tcpHost: TCP_SERVER_HOST, tcpPort: TCP_SERVER_PORT });
});

app.post('/command', async (req, res) => {
  const command = (req.body?.command || '').toString().trim();
  const expectEnd = Boolean(req.body?.expectEnd);

  if (!command) {
    res.status(400).json({ error: 'Comando mancante' });
    return;
  }

  try {
    const response = await sendCommandTcp(command, expectEnd);
    res.json({ response });
  } catch (error) {
    res.status(502).json({ error: error.message || 'Errore bridge TCP' });
  }
});

app.listen(BRIDGE_PORT, () => {
  // eslint-disable-next-line no-console
  console.log(`Bridge HTTP->TCP attivo su :${BRIDGE_PORT}, target ${TCP_SERVER_HOST}:${TCP_SERVER_PORT}`);
});
