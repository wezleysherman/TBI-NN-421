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
import tensorflow as tf

class BSSCS_UNET:
	def __init__(self, learning_rate=0.001):
		self.learning_rate = learning_rate

	def generate_unet_arch(self, input):
		''' Handles generating a TF Implementation of a UNET utilizing the architecture discussed in
		    "U-Net: Convolutional Networks for Biomedical Image Segmentation" by Ronneberger, Fischer, and Brox

		    While the code for the implementation is 100% ours, the architecture for the UNET is not. We are not claiming any ownership for the architecture. 
		    Implementing the UNET arch is comparable to implementing selection sort.

		    tf.layers.conv2d docs: https://www.tensorflow.org/api_docs/python/tf/layers/conv2d
			tf.layers.conv2d_transpost for up convolutions: https://www.tensorflow.org/api_docs/python/tf/layers/conv2d_transpose
			tf.concat for the copy and crop methods: https://www.tensorflow.org/api_docs/python/tf/concat
			Hopefully I implemented this correctly  ¯\_(ツ)_/¯

			Notes after implementing: The architecture mentioned a 'copy and crop' -.. I know that with tf.concat we are essentially performing the 'copy'
			by concatinating the two sets of matricies. However; I'm kinda at a loss as to how the 'crop' is supposed to happen... Actually, the crop
			is because we are trying to concat matricies of different sizes. This probably wont work without the crop. TO-DO: Figure out the crop.
		'''
		# first block in UNET --> Concat with the final block
		convolution_layer_1 = tf.layers.conv2d(inputs=input, filters=64, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_2 = tf.layers.conv2d(inputs=convolution_layer_1, filters=64, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		max_pooling_layer_1 = tf.layers.max_pooling2d(inputs=convolution_layer_2, pool_size=[2, 2], strides=1, padding="VALID")

		# second block in UNET --> Concat with second to final block
		convolution_layer_3 = tf.layers.conv2d(inputs=max_pooling_layer_1, filters=128, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_4 = tf.layers.conv2d(inputs=convolution_layer_3, filters=128, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		max_pooling_layer_2 = tf.layers.max_pooling2d(inputs=convolution_layer_4, pool_size=[2, 2], strides=1, padding="VALID")

		# third block in UNET --> Concat with third from final block
		convolution_layer_5 = tf.layers.conv2d(inputs=max_pooling_layer_2, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_6 = tf.layers.conv2d(inputs=convolution_layer_5, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		max_pooling_layer_3 = tf.layers.max_pooling2d(inputs=convolution_layer_6, pool_size=[2, 2], strides=1, padding="VALID")

		# fourth block in UNET --> Concat with fourth from final block
		convolution_layer_7 = tf.layers.conv2d(inputs=max_pooling_layer_3, filters=512, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_8 = tf.layers.conv2d(inputs=convolution_layer_7, filters=512, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		max_pooling_layer_4 = tf.layers.max_pooling2d(inputs=convolution_layer_8, pool_size=[2, 2], strides=1, padding="VALID")

		# middle UNET block
		convolution_layer_9 = tf.layers.conv2d(inputs=max_pooling_layer_4, filters=1024, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_10 = tf.layers.conv2d(inputs=convolution_layer_9, filters=1024, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_1 = tf.layers.conv2d_transpose(inputs=convolution_layer_10, filters=1024, kernel_size=[2, 2], strides=1, padding="SAME")
		
		# fourth from last 
		# Is this how you do it? -- TO-DO: Double check this concat -.. I'm only copying; not cropping here.
		concat_layer_1 = tf.concat([convolution_up_1, convolution_layer_8], axis=1)
		convolution_layer_11 = tf.layers.conv2d(inputs=concat_layer_1, filters=512, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_12 = tf.layers.conv2d(inputs=convolution_layer_11, filters=512, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_2 = tf.layers.conv2d_transpose(inputs=convolution_layer_12, filters=512, kernel_size=[2, 2], strides=1, padding="SAME")

		# third from last
		# Is this how you do it? -- TO-DO: Double check this concat -.. I'm only copying; not cropping here.
		concat_layer_1 = tf.concat([convolution_up_2, convolution_layer_6], axis=1)
		convolution_layer_13 = tf.layers.conv2d(inputs=convolution_up_2, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_14 = tf.layers.conv2d(inputs=convolution_layer_13, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_3 = tf.layers.conv2d_transpose(inputs=convolution_layer_14, filters=256, kernel_size=[2, 2], strides=1, padding="SAME")

		# second from last
		# Is this how you do it? -- TO-DO: Double check this concat -.. I'm only copying; not cropping here.
		concat_layer_1 = tf.concat([convolution_up_3, convolution_layer_4], axis=1)
		convolution_layer_15 = tf.layers.conv2d(inputs=convolution_up_3, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_16 = tf.layers.conv2d(inputs=convolution_layer_15, filters=128, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_4 = tf.layers.conv2d_transpose(inputs=convolution_layer_16, filters=128, kernel_size=[2, 2], strides=1, padding="SAME")

		# last block
		# Is this how you do it? -- TO-DO: Double check this concat -.. I'm only copying; not cropping here.
		concat_layer_1 = tf.concat([convolution_up_4, convolution_layer_2], axis=1)
		convolution_layer_17 = tf.layers.conv2d(inputs=convolution_up_4, filters=128, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_18 = tf.layers.conv2d(inputs=convolution_layer_17, filters=64, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_19 = tf.layers.conv2d(inputs=convolution_layer_18, filters=64, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_5 = tf.layers.conv2d_transpose(inputs=convolution_layer_19, filters=2, kernel_size=[1, 1], strides=1, padding="SAME")

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

# quick test for errors
bsscs = BSSCS_UNET()
input_ph = tf.placeholder(tf.float32, shape=[None, 572, 572, 1])
# Oh boy, this is scary
bsscs.generate_unet_arch(input_ph)