import scipy.stats as stat
import numpy as np
import matplotlib.pyplot as plt
x = np.arange(-4, 4, 0.02)
y1 = stat.uniform.pdf(x)
y2 = stat.uniform.cdf(x)
ax1 = plt.axes([0, 0, 1, 1])
ax1.plot(x, y1)
plt.show()