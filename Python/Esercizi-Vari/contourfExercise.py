import matplotlib.pyplot as plt
from matplotlib import cm
import numpy as np
z = np.load('heights.npy')
#cs = plt.contourf(z, 4, colors = ('k', 'b', 'c', 'g', 'brown'))
cs = plt.contourf(z, 5, cmap = cm.coolwarm)
plt.title("Let's do this other plot", size = 15)
#plt.clabel(cs, inline=1, fontsize=14)
cbar = plt.colorbar(cs, orientation='horizontal')
cbar.ax.tick_params(labelsize=15)
plt.xticks([])
plt.yticks([])
plt.show()