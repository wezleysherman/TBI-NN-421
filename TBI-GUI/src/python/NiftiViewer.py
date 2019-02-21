import nibabel
import matplotlib.pyplot as plt
from matplotlib.pyplot import plot, ion, show
from matplotlib.widgets import Slider
import os
import sys
import socket
'''
Used nipy site as reference when displaying. http://nipy.org/nibabel/coordinate_systems.html
'''
current_index = 0
plt.rcParams['toolbar'] = 'None'
fig, axes = plt.subplots()
HOST = "127.0.0.1"
PORT = 8080

def update(val):
    global current_index, axes
    im.set_data(data_arr[int(val)])
    plt.draw()


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(("127.0.0.1", 8080))

file = sys.argv[1]
image = nibabel.load(file)
data_arr = image.get_data().T
global im
im = axes.imshow(data_arr[0])
sliderAxe = plt.axes([0.15, 0.02, 0.65, 0.03])
slice = Slider(sliderAxe, "Slice:", 0, 21, 0, '%1.2f', valstep=1)
slice.on_changed(update)
plt.show()