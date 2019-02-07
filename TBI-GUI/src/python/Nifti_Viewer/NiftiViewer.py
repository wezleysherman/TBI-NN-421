import nibabel
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import threading
import time

image = nibabel.load("../../resources/knee.nii")
data = image.get_fdata()
slice_0 = data[300, :, :]
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
    main()