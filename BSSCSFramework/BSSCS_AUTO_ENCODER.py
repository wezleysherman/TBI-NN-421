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
	def __init__ (self, l2_reg, learning_rate, steps, batch_size):
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

	def create_layer(input, nuerons, activation=tf.nn.relu):
		# To-Do: implement
		return

	def get_partial():
		# To-Do: implement
		return

	def create_autoencoder(neurons, input=None):
		#To-Do: implement
		return