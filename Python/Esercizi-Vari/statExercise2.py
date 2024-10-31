import scipy.stats as stat
import numpy as np
import matplotlib.pyplot as plt

x = np.arange(-4, 4, 0.02)
y = []
y.append(stat.uniform.pdf(x, -3, 6))
y.append(stat.uniform.cdf(x, -3, 6))
y.append(stat.norm.pdf(x))
y.append(stat.norm.cdf(x))

plt.rc('xtick', labelsize = 15)
plt.rc('ytick', labelsize = 15)
fig, ax = plt.subplots(2, 2, sharex=True)
ax[0, 0].plot(x, y[0])
ax[0, 0].set_ylabel('Uniform PDF(x)', size = 15)
ax[0, 1].plot(x, y[1])
ax[0, 1].set_ylabel('Uniform CDF(x)', size = 15)
ax[1, 0].plot(x, y[2])
ax[1, 0].set_ylabel('Normal PDF(x)', size = 15)
ax[1, 0].set_xlabel('Value', size = 15)
ax[1, 1].plot(x, y[3])
ax[1, 1].set_ylabel('Normal CDF(x)', size = 15)
ax[1, 1].set_xlabel('Value', size = 15)

plt.show()