import numpy as np
import matplotlib.pyplot as plt
sigma = 2
n = 5*sigma
x = np.arange(-n, n+0.1, 0.01)
g = np.ones(x.shape)
g = (1/(sigma * np.sqrt(2 * np.pi))) * (-1 * np.exp(x**2/ (2*sigma^2)))
g = g.reshape(len(g),1)
g2 = g.dot(np.transpose(g))
plt.contour(np.transpose(g2))
plt.show()