# Authors: Wezley Sherman
#
# Reference Attributes: U-Net: Convolutional Networks for Biomedical Image Segmentation
# By: Olaf Ronneberger, Philipp Fischer, and Thomas Brox
#
# This class is a part of the BSSCS Net Framework to import DICOM images and batch them for the UNET
#
# BSSCS Docs Importer location: TBD
#
# Citation:
# Ronneberger, et al. “U-Net: Convolutional Networks for Biomedical Image Segmentation.” 
#     [Astro-Ph/0005112] A Determination of the Hubble Constant from Cepheid Distances and a Model of the Local Peculiar Velocity Field, 
#     American Physical Society, 18 May 2015, arxiv.org/abs/1505.04597.
# Site:
# https://arxiv.org/pdf/1505.04597.pdf

class BSSCS_UNET:
	def __init__(self, learning_rate=0.001):
		self.learning_rate = learning_rate

	def generate_unet_arch(self):
		''' Handles generating a TF Implementation of a UNET utilizing the architecture discussed in
		    "U-Net: Convolutional Networks for Biomedical Image Segmentation" by Ronneberger, Fischer, and Brox
		'''
		return None

	def train_unet(self):
		''' Handles training a UNET based off the data fed to it
		'''
		return None

	def test_unet(self):
		''' Runs a trained UNET through an evaluation/test phase to detect errors
		'''
		return None

	def save_graph(self):
		''' Saves a UNET graph
		'''
		return None

	def load_graph(self):
		''' Loads a UNET graph
		'''
		return None