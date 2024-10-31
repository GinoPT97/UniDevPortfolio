import scipy.stats as stat
import numpy as np
import matplotlib.pyplot as plt
# LET'S CREATE THE RANDOM VARIABLES
y = []
ys = []
y.append(stat.norm.rvs(1, 1, size = 1000))
ys.append(stat.describe(y[0]))
y.append(stat.norm.rvs(5, 1, size = 1000))
ys.append(stat.describe(y[1]))
y.append(stat.norm.rvs(5, 5, size = 1000))
ys.append(stat.describe(y[2]))

# LET'S MAKE THE PLOT
fig = plt.figure() # Create figure
plt.rc('xtick', labelsize = 15)
plt.rc('ytick', labelsize = 15)
ax = fig.add_axes([0.15, 0.15, 0.75, 0.75])
ax.set_title("Three different normal distributions", size = 15)
ax.set_xlabel('Sample', size = 15)
ax.set_ylabel('Value', size = 15)
ax.plot(y[0], c = 'c', ls = '-', lw = 2,
        marker = '+', ms = 15, mew = 3)
ax.plot(y[1], c = 'b', ls = '--', lw = 2,
        marker = 'x', ms = 15, mew = 3)
ax.plot(y[2], c = 'r', ls = 'none', lw = 2,
        marker = '.', ms = 15, mew = 3)
yl = []
for i in range(3):
    yl.append('loc = ' + str(np.around(ys[i][2], 2)) +
              ' scale = ' + str(np.around(np.sqrt(ys[i][3]), 2)))
plt.legend(yl, prop = dict(size = 15), loc = 0)
plt.show()