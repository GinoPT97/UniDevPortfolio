import scipy.stats as stat
import numpy as np
import matplotlib.pyplot as plt
x = np.arange(0, 12, 0.01)
y1 = stat.uniform.pdf(x, 1, 10)
y2 = stat.uniform.cdf(x, 1, 10)
plt.figure()
plt.plot(x, y1)
plt.figure()
plt.plot(x, y2)
plt.show()