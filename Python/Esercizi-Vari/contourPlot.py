import matplotlib.pyplot as plt
import numpy as np
z = np.load('heights.npy')
cs = plt.contour(z)
plt.clabel(cs, fmt = '%.0f', inline = True)
#plt.show()
