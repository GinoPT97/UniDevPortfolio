import scipy.stats as stat
import numpy as np
import matplotlib.pyplot as plt
x = np.arange(-4, 4, 0.02)
y1 = stat.uniform.pdf(x)
y2 = stat.uniform.cdf(x)
y3 = stat.norm.pdf(x)
y4 = stat.norm.cdf(x)
ax1 = plt.subplot(221)
ax2 = plt.subplot(222)
ax3 = plt.subplot(223)
ax4 = plt.subplot(224)
ax1.plot(x, y1)
ax2.plot(x, y2)
ax3.plot(x, y3)
ax4.plot(x, y4)
plt.show()