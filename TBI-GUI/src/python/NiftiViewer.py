import nibabel
import matplotlib.pyplot as plt
from matplotlib.pyplot import plot, ion, show
from matplotlib.widgets import Slider
import matplotlib.animation as animation
import threading
import time
import os
import socket

current_index = 0
plt.rcParams['toolbar'] = 'None'
fig, axes = plt.subplots()
HOST = "127.0.0.1"
PORT = 8080

def update(val):
    global current_index, axes
    im.set_data(data_arr[int(val)])
    plt.draw()

image = nibabel.load(os.path.dirname(os.path.abspath(__file__)).replace("\\", "/") + "/../resources/knee.nii")
data_arr = image.get_data().T
global im
im = axes.imshow(data_arr[0])
sliderAxe = plt.axes([0.15, 0.02, 0.65, 0.03])
slice = Slider(sliderAxe, "Slice:", 0, 21, 0, '%1.2f', valstep=1)
slice.on_changed(update)
plt.show()