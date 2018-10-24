# Authors: Wezley Sherman
#
# Reference Attributes: Official TensorFlow documentation from Google
# TF conv2d documention:
# https://www.tensorflow.org/api_docs/python/tf/layers/conv2d
# TF Max pooling documentation:
# https://www.tensorflow.org/api_docs/python/tf/layers/max_pooling2d

import tensorflow as tf
import numpy as np

class BSSCS_CNN:
	def create_cnn_block(self, filters, kernel_size, cnn_strides, pool_size, pooling_strides, cnn_padding='SAME', cnn_activation=tf.nn.relu, pooling_padding='VALID', input_shape=None, input=None):
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
		output_layers = []
		if(input_shape):
			input = tf.placeholder(tf.float32, shape=input_shape)
			output_layers.append(input)
		convolution_layer = tf.layers.conv2d(input, filters, kernel_size, cnn_strides, cnn_padding, activation=cnn_activation)
		max_pooling_layer = tf.layers.max_pooling2d(convolution_layer, pool_size, pooling_strides, pooling_padding)
		output_layers.append(convolution_layer)
		output_layers.append(max_pooling_layer)
		return output_layers

			
	def create_partial_pooling(self, pool_size, pooling_strides, pooling_padding='VALID', input_shape=None, input=None):
		''' BSSCS create partial pooling layer -- convenience method
			
			Handles the creation of a 2D pooling layers

			Input:
				- input: accepts a convolutional or pooling layer
				- pool_size: indicates the size of the pool for averaging (E.g. if we have an array of [1, 3, 2, 4] max pooling will shrink it to the max value [4])
				- pooling_strides: a single number indicating stride transform (e.g. how many pixels are traversed with a pooling filter)
				- pooling_padding: defines what we do with padding of the image when a kernel is outside image bounds ('SAME' or 'VALID')
			Returns:
				- Max pooling layer to input specifications
		'''
		output_layers = []
		if(input_shape):
			input = tf.placeholder(tf.float32, shape=input_shape)
			output_layers.append(input)
		output_layers.append(tf.layers.max_pooling2d(input, pool_size, pooling_strides, pooling_padding))
		return output_layers

	def create_partial_cnn(self, filters, kernel_size, cnn_strides, cnn_padding='SAME', cnn_activation=tf.nn.relu, input_shape=None, input=None):
		''' BSSCS create a partial cnn layer -- convenience method

			Input:
				- input: accepts a tensorflow placeholder of rank 4 (e.g. [batch_size, im_width, im_height, im_channels])
				- filters: defines how many filters we want for the convolutional layer
				- kernel_size: tuple containing filter size (e.g. (3, 3) for a 3x3 filter)
				- cnn_strides: a single number indicating stride transform (e.g. how many pixels are traversed with a filter)
				- cnn_padding: defines what we do with padding of the image when a kernel is outside image bounds ('SAME' or 'VALID')
				- cnn_activation: defines the activation function for the convolutional layer
			Returns:
				- Array CNN layer to input specifications
		'''
		output_layers = []
		if(input_shape):
			input = tf.placeholder(tf.float32, shape=input_shape)
			output_layers.append(input)
		output_layers.append(tf.layers.conv2d(input, filters, kernel_size, cnn_strides, cnn_padding, activation=cnn_activation))
		return output_layers

	def create_convolution_network(self, input_shape, cnn_kernels, cnn_filters, pooling_filters, strides=2):
		''' BSSCS create a cnn network

			Input:
				- input_image_size: accepts an array specifying batch size, width, height, and channels for the image(s)
				- cnn_kernels: array of filter sizes of the cnn
				- cnn_filters: array of filter numbers for each cnn layer
				- pooling_filters: 2d array of pooling sizes for the pooling layers
				- strides: number of strides for each layer
			Returns:
				- array of cnn and max pooling layers tied together
		'''
		input_placeholder = tf.placeholder(tf.float32, shape=input_shape)
		conv_layers = [input_placeholder]
		last_layer = input_placeholder
		for i in range(0, len(cnn_kernels)):
			cnn_kernel = cnn_kernels[i]
			cnn_filter = cnn_filters[i]
			pooling_size = pooling_filters[i]
			layers = self.create_cnn_block(input=last_layer, filters=cnn_filter, kernel_size=cnn_kernel, cnn_strides=strides, pool_size=pooling_size, pooling_strides=strides)
			cnn_layer, max_pooling_layer = layers[0], layers[1]
			conv_layers.append(cnn_layer)
			conv_layers.append(max_pooling_layer)
			last_layer = max_pooling_layer
		return conv_layers

	def get_convolutional_filters(self, input_image, conv_net):
		''' BSSCS get a visualization of the output of the cnn

			Input:
				- input_image: accepts a pixel array for input
				- conv_net: accepts the convolutional network layers (in an array) as input:
			Returns:
				- 4D array of filter data
		'''
		output_images = []
		cnn_input_layer = conv_net[len(conv_net)-1]
		input_image = np.reshape(input_image, (1, input_image.shape[0], input_image.shape[1], 1))
		with tf.Session() as session:
			tf.global_variables_initializer().run()
			cnn_out = session.run(cnn_input_layer, feed_dict={conv_net[0]:input_image})
			for i in range(0, len(cnn_out[0])):
				output_images.append(cnn_out[0, :, :, i])
		return output_images