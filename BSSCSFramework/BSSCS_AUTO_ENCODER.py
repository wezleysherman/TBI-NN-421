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

	def create_layer(self, neurons, input=None):
		'''
		'''
		if not input:
			input = tf.placeholder(tf.float32, shape=[None, neurons])
		new_layer = tf.layers.dense(inputs=input, units=neurons, activation=self.activation, kernel_regularizer=self.l2_reg)
		return new_layer

	def get_partial():
		# To-Do: implement
		return

	def create_autoencoder(self, neurons, input=None):
		'''
		'''
		nodes = []
		if not input:
			input = tf.placeholder(tf.float32, shape=[None, neurons[0]])
		nodes.append(input)
		for layer in neurons:
			new_layer = tf.layers.dense(inputs=input, units=layer, activation=self.activation, kernel_regularizer=self.l2_reg)
			nodes.append(new_layer)
			input = new_layer
		return	nodes