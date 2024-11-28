import http from 'http';

const PORT = 3000;

let server = http.createServer(function(request, response){
  response.writeHead(200, {'Content-Type': 'text/html'});
  response.write(`
  <!DOCTYPE html>
  <html>
    <body><h1>Hello Web Tech!</h1></body>
  </html>`);
  response.end();
}).listen(PORT);

console.log(`Web app listening on port ${PORT}`);