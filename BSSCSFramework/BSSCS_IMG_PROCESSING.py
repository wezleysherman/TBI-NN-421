# Authors: Wezley Sherman
#
# Reference Attributes: 
# PIL Documentation:
# https://pillow.readthedocs.io/en/3.0.x/reference/

import tensorflow as tf
import numpy as np
from PIL import ImageEnhance, Image

class BSSCS_IMG_PROCESSING:
	def contrast_image(pixel_arrs, contrast_val):
		''' Perform contrast adjustment on image.

			Input:
				- pixel_arrs: an array of pixel arrays for the image we are adjusting
				- contrast_val: the contrast value we want to apply to the images
			Output:
				- Array of pixel arrays with constrast performed to it.
		'''
		return_arr = []
		for arr in pixel_arrs:
			img = Image.fromarray(arr.astype('uint8')).convert('RGBA')
			img_enhance = ImageEnhance.Contrast(img)
			arr = img_enhance.enhance(contrast_val)
			return_arr.append(arr)
		return return_arr

	def scale_image(pixel_arrs, scale_vector):
		return

	def crop_image(pixel_arrs, crop_vector):
		return