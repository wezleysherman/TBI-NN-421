# Authors: Wezley Sherman
#
# Reference Attributes: Official TensorFlow documentation from Google
# TF conv2d documention:
# https://www.tensorflow.org/api_docs/python/tf/layers/conv2d
# TF Max pooling documentation:
# https://www.tensorflow.org/api_docs/python/tf/layers/max_pooling2d

import tensorflow as tf

class BSSCS_CNN:
	def create_cnn_block(self, input, filters, kernel_size, cnn_strides, pool_size, pooling_strides, cnn_padding='SAME', cnn_activation=tf.nn.relu, pooling_padding='VALID'):
		''' BSSCS create cnn block method

			Handles creation of a 2D convolutional 'block'
			A convolutional 'block' consists of both a convolutional layer
			and a max pooling layer.
	
			Input:
				CNN: 
					- input: accepts a tensorflow placeholder of rank 4 (e.g. [batch_size, im_width, im_height, im_channels])
					- filters: defines how many filters we want for the convolutional layer
					- kernel_size: tuple containing filter size (e.g. (3, 3) for a 3x3 filter)
					- cnn_strides: a single number indicating stride transform (e.g. how many pixels are traversed with a filter)
					- cnn_padding: defines what we do with padding of the image when a kernel is outside image bounds ('SAME' or 'VALID')
					- cnn_activation: defines the activation function for the convolutional layer
				Max_Pooling:
					- pool_size: indicates the size of the pool for averaging (E.g. if we have an array of [1, 3, 2, 4] max pooling will shrink it to the max value [4])
					- pooling_strides: a single number indicating stride transform (e.g. how many pixels are traversed with a pooling filter)
					- pooling_padding: defines what we do with padding of the image when a kernel is outside image bounds ('SAME' or 'VALID')
			Returns:
				cnn layer and max pooling layer
		'''
		convolution_layer = tf.layers.conv2d(input, filters, kernel_size, cnn_strides, cnn_padding, activation=cnn_activation)
		max_pooling_layer = tf.layers.max_pooling2d(convolution_layer, pool_size, pooling_strides, pooling_padding)
		return convolution_layer, max_pooling_layer

			
	def create_partial_pooling(self, input, pool_size, pooling_strides, pooling_padding='VALID'):
		''' BSSCS create partial pooling layer
			
			Handles the creation of a 2D pooling layers

			Input:
				- input: accepts a convolutional or pooling layer
				- pool_size: indicates the size of the pool for averaging (E.g. if we have an array of [1, 3, 2, 4] max pooling will shrink it to the max value [4])
				- pooling_strides: a single number indicating stride transform (e.g. how many pixels are traversed with a pooling filter)
				- pooling_padding: defines what we do with padding of the image when a kernel is outside image bounds ('SAME' or 'VALID')
			Returns:
				- Max pooling layer to input specifications
		'''
		return tf.layers.max_pooling2d(input, pool_size, pooling_strides, pooling_padding)

	def create_partial_cnn(self, input, filters, kernel_size, cnn_strides, cnn_padding='SAME', cnn_activation=tf.nn.relu):
		''' BSSCS create a partial cnn layer

			Input:
				- input: accepts a tensorflow placeholder of rank 4 (e.g. [batch_size, im_width, im_height, im_channels])
				- filters: defines how many filters we want for the convolutional layer
				- kernel_size: tuple containing filter size (e.g. (3, 3) for a 3x3 filter)
				- cnn_strides: a single number indicating stride transform (e.g. how many pixels are traversed with a filter)
				- cnn_padding: defines what we do with padding of the image when a kernel is outside image bounds ('SAME' or 'VALID')
				- cnn_activation: defines the activation function for the convolutional layer
			Returns:
				- CNN layer to input specifications
		'''
		return tf.layers.conv2d(input, filters, kernel_size, cnn_strides, cnn_padding, activation=cnn_activation)

	def create_convolution_network(self, input, cnn_kernels, cnn_filters, pooling_filters, strides=2):
		''' BSSCS create a cnn network

			Input:
				- input: accepts a tensorflow placeholder of rank 4 (e.g. [batch_size, im_width, im_height, im_channels])
				- cnn_kernels: array of filter sizes of the cnn
				- cnn_filters: array of filter numbers for each cnn layer
				- pooling_filters: 2d array of pooling sizes for the pooling layers
				- strides: number of strides for each layer
			Returns:
				- array of cnn and max pooling layers tied together
		'''
		conv_layers = []
		last_layer = input
		for i in range(0, len(cnn_kernels)):
			cnn_kernel = cnn_kernels[i]
			cnn_filter = cnn_filters[i]
			pooling_size = pooling_filters[i]
			cnn_layer, max_pooling_layer = self.create_cnn_block(input=last_layer, filters=cnn_filter, kernel_size=cnn_kernel, cnn_strides=strides, pool_size=pooling_size, pooling_strides=strides)
			conv_layers.append(cnn_layer)
			conv_layers.append(max_pooling_layer)
			last_layer = max_pooling_layer
		return conv_layers
