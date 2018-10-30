# Authors: Wezley Sherman
#
# Reference Attributes: 
# PIL Documentation:
# https://pillow.readthedocs.io/en/3.0.x/reference/

import tensorflow as tf
import numpy as np
from PIL import ImageEnhance, Image
import math

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
			img = Image.fromarray(arr.astype('uint8')).convert('RGB')
			img_enhance = ImageEnhance.Contrast(img)
			arr = img_enhance.enhance(contrast_val)
			return_arr.append(arr)
		return return_arr

	def scale_image(pixel_arrs, scale_by):
		''' Perform scale adjustment on image.

			Input:
				- pixel_arrs: an array of pixel arrays for the image we are adjusting
				- scale_by: The scale value we want for the image (multiple)
			Output:
				- Array of pixel arrays with scale performed to it.
		'''
		return_arr = []
		for arr in pixel_arrs:
			img_width, img_height = arr.shape
			img_width = math.floor(img_width * scale_by)
			img_height = math.floor(img_height * scale_by)

			img = Image.fromarray(arr.astype('uint8')).convert('RGB')
			img = img.resize((img_width, img_height), Image.ANTIALIAS)
			return_arr.append(img)
		return return_arr

	def crop_image(pixel_arrs, crop_vector):
		return_arr = []
		return return_arr