# Authors: Wezley Sherman
#
# Reference Attributes: Official TensorFlow documentation from Google
# Dense layers:
# https://www.tensorflow.org/api_docs/python/tf/layers/dense
# l2 Regularizer:
# https://www.tensorflow.org/api_docs/python/tf/contrib/layers/l2_regularizer
# Tensorflow Relu activation:
# https://www.tensorflow.org/api_docs/python/tf/nn/relu

import tensorflow as tf
import math

class BSSCS_AUTO_ENCODER:
	def __init__ (self, l2_reg, learning_rate, steps, batch_size, activation):
		''' Constructor for the BSSCS autoencoder object
			
			The purpose of this is to set initial hyperparameters on the creation
			of an autoencoder. 

			l2 reg and learning rate should be easy to adjust during the training process for the
			nueral network.
		'''
		self.l2_reg = l2_reg
		self.learning_rate = learning_rate
		self.steps = steps
		self.batch_size = batch_size
		self.activation = activation
		self.layers = []

	def create_layer(self, neurons, input=None):
		''' Handles creating a single dense layer for the autoencoder

			Inputs:
				- neurons: an int containing the number of neurons we want the layer to contain
				- input: Takes a tensor object for an input (should probably be a placeholder)

			Returns:
				- A single tensor object for the layer
		'''
		if input is None:
			input = tf.placeholder(tf.float32, shape=[None, neurons])
		new_layer = tf.layers.dense(inputs=input, units=neurons, activation=self.activation, kernel_regularizer=self.l2_reg)
		return new_layer

	def get_partial(self):
		''' Handles retreiving the middle layer of the autoencoder (most condensed representation)

			Returns:
				- Single tensor for the middle layer of the autoencoder
		'''
		middle_idx = math.ceil(len(self.layers)/2)
		return self.layers[middle_idx]

	def create_autoencoder(self, neurons, input=None):
		''' Handles creating a full autoencoder graph to train a model

			Inputs:
				- neurons: an array containing the sizes for each layer (eg. [512, 256, 128, 256, 512] is an autoencoder with 5 layers)
				- input: Takes in a tensor object for an input (should probably be a placeholder)

			Returns:
				- Array continaing all layers within the TF graph. (Neural Network) 
		'''
		nodes = []
		if input is None:
			input = tf.placeholder(tf.float32, shape=[None, neurons[0]])
		nodes.append(input)
		for layer in neurons:
			new_layer = self.create_layer(neurons=layer, input=input)
			nodes.append(new_layer)
			input = new_layer
		self.layers = nodes
		return	nodes