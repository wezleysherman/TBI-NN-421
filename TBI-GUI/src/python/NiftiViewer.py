import nibabel
import matplotlib.pyplot as plt
from matplotlib.pyplot import plot, ion, show
import matplotlib.animation as animation
import threading
import time
import os

current_index = 0

def prev_slice(axes):
    global current_index
    current_index = current_index % data_arr.shape[0]
    axes.images[0].set_array(data_arr[current_index])
    axes.imshow(data_arr[current_index])
    current_index += 1

def next_slice(axes):
    pass

def switch_slice():
    while current_index < 22:
        time.sleep(.5)
        prev_slice(axes)
        plt.draw()
    return

image = nibabel.load(os.path.dirname(os.path.abspath(__file__)).replace("\\", "/") + "/../resources/knee.nii")
data_arr = image.get_data().T
fig, axes = plt.subplots()
axes.imshow(data_arr[0])
slice_switch = threading.Thread(name="Switch", target=switch_slice)
slice_switch.start()
plt.show()

'''slice_0 = data[300, :, :]
slice_1 = data[:, 300, :]
slice_2 = data[:, :, 5]

def show_slices(slices):
   """ Function to display row of image slices """
   fig, axes = plt.subplots(1, len(slices))
   for i, slice in enumerate(slices):
       axes[i].imshow(slice.T, cmap="gray", origin="lower")


class DisplayImage(threading.Thread):
    def run(self):
        print(data.shape)
        show_slices([slice_0, slice_1, slice_2])
        plt.suptitle("Center slices for EPI image")
        plt.show()

def main():
    thread = DisplayImage()
    thread.start()
    time.sleep(6)
    slice_0 = data[300, :, :]
    slice_1 = data[:, 300, :]
    slice_2 = data[:, :, 15]
    show_slices([slice_0, slice_1, slice_2])

if __name__ == '__main__':
    main()'''