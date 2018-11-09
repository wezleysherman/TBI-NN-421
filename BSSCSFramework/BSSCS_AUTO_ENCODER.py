# Authors: Wezley Sherman
#
# Reference Attributes: Official TensorFlow documentation from Google
# Dense layers:
# https://www.tensorflow.org/api_docs/python/tf/layers/dense
# l2 Regularizer:
# https://www.tensorflow.org/api_docs/python/tf/contrib/layers/l2_regularizer
# Tensorflow Relu activation:
# https://www.tensorflow.org/api_docs/python/tf/nn/relu
# Optimizer and Gradient Descent:
# https://www.tensorflow.org/api_docs/python/tf/train/Optimizer
# Python Enum documentation:
# https://docs.python.org/3/library/enum.html

import config
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

	def create_loss_function(input, labels, loss=Loss_Functions.SIGMOID):
		''' Default loss function will be sigmoid cross entropy
			
			Inputs:
				- loss: Loss function to use from enum LOSS_FUNCTIONS in config.py
				- input: Input tensor of the final output from NN
				- labels: Associated labels to the input data

			Returns:
				- Instance of softmax_cross_entropy_with_logits_v2 loss function

			TensorFlow documentation: 
				Softmax v2:
				https://www.tensorflow.org/api_docs/python/tf/nn/softmax_cross_entropy_with_logits_v2

				Sigmoid:
				https://www.tensorflow.org/api_docs/python/tf/nn/sigmoid_cross_entropy_with_logits
		'''
		set_loss = tf.nn.sigmoid_cross_entropy_with_logits(logits=input, labels=labels)

		if(loss is LOSS_FUNCTIONS.SOFTMAX):
			set_loss = tf.nn.softmax_cross_entropy_with_logits_v2(logits=input, labels=labels)

		self.loss = set_loss
		return set_loss

	def create_optimizer(optimizer=Optimizers.ADAM):
		''' Default optimizer will be AdamOptmizer

			Returns:
				- Instance of AdamOptimizer with learning rate set

			TensorFlow documentation: 

				Adam:
				https://www.tensorflow.org/api_docs/python/tf/train/AdamOptimizer

				Gradient Descent:
				https://www.tensorflow.org/api_docs/python/tf/train/GradientDescentOptimizer
		'''
		bsscs_optmizer = tf.train.AdamOptimizer(learning_rate=self.learning_rate)

		if(optimizer is Optimizers.GRADIENT_DESCENT):
			bsscs_optmizer = tf.train.GradientDescentOptimizer(learning_rate=self.learning_rate)

		self.optimizer = bsscs_optmizer
		return bsscs_optmizer

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