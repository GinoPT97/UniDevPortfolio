import matplotlib.pyplot as plt
import numpy as np
z = np.load('heights.npy')
cs = plt.contourf(z)
plt.colorbar()
plt.show()