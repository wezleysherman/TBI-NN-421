# Authors: Wezley Sherman
#
# Reference Attributes: Official TensorFlow documentation from Google
# Dense layers:
# https://www.tensorflow.org/api_docs/python/tf/layers/dense
# l2 Regularizer:
# https://www.tensorflow.org/api_docs/python/tf/contrib/layers/l2_regularizer
# Tensorflow Relu activation:
# https://www.tensorflow.org/api_docs/python/tf/nn/relu
#
# BSSCS Docs Classifier location: BSSCS_DOCS/class.html
import tensorflow as tf

class BSSCS_CLASSIFIER:
	def __init__(self, l2_reg, learning_rate, steps, batch, loss=None, optimizer=None):
		''' Constructor for the BSSCS autoencoder object
			
			The purpose of this is to set initial hyperparameters on the creation
			of an autoencoder. 

			l2 reg and learning rate should be easy to adjust during the training process for the
			nueral network.
		'''
		self.l2_reg = l2_reg
		self.learning_rate = learning_rate
		self.steps = steps
		self.batch = batch
		self.loss = loss
		self.optimizer = optimizer
		self.layers = []

	def connect_conv_net(self, conv_graph):
		''' Handles connecting the classifier to the conv network input.
			Fancy way of handling array creation?

			See: BSSCS_CNN.py for more info

			Inputs:
				- conv_graph: A graph of tensor objects for the convolutional neural network

			Returns:
				- Array containing both a graph for the conv network and autoencoder
					[classifier, conv_network]
		'''
		return_arr = [self.layers, conv_graph]
		return return_arr

	def get_optimizer(self):
		''' Handkles returning the optmizer for the class

			Returns:
				- Optimizer object
		'''
		return self.optimizer
		
	def get_regularizer(self):
		''' Handles returning the regularizer

			Returns:
				- Regularizer object
		'''
		return self.l2_reg

	def set_regularizer(self, l2_reg):
		''' Handles setting a regularizer

			Input:
				- l2_reg: regularizer we want to be set.

			Returns:
				- Regularizer that we've set
		'''
		self.l2_reg = l2_reg
		return l2_reg

	def get_learning_rate(self):
		''' Returns the current learning rate of the classifier object.

			Returns:
				- Returns the learning rate
		'''
		return self.learning_rate

	def set_learning_rate(self, learning_rate):
		''' Handles setting the learning rate for the object

			Input:
				- learning_rate: a number indicating the new learning rate (Should be between 0.0001 and 0.000001)

			Returns:
				- The learning rate that's been set
		'''
		self.learning_rate = learning_rate
		return self.learning_rate

	def get_steps(self):
		''' Handles returning the number of training steps for the classifier.

			Returns:
				- Number of training steps it will take
		'''
		return steps

	def set_steps(self, steps):
		''' Handles setting the number of training steps

			Input:
				- steps: number of training steps to be set.

			Returns:
				- Number of steps
		'''
		self.steps = steps
		return self.steps

	def get_layers(self):
		''' Handles returning all of the layers in the classifier

			Returns:
				- Array of tensor objects
		'''
		return self.layers

	def get_loss(self):
		''' Handles returning a loss function

			Returns:
				- Tensor for loss
		'''
		return self.loss

	def create_layer(self, neurons, activation=tf.nn.relu, input=None):
		''' Handles the creation of a single layer within the fully connected network

			Inputs:
				- neurons: number of neurons we want within the layer
				- activation: the activation to use for the layer (default: relu)
				- input: Input tensor object for the layer

			Returns:
				- A single layer for a FC graph
		'''
		if input is None:
			input = tf.placeholder(tf.float32, shape=[None, neurons])
		layer = tf.layers.dense(inputs=input, units=neurons, activation=activation)
		return layer

	def create_loss_function(self, input, labels):
		''' Default loss function will be softmax cross entropy
			
			Returns:
				- Instance of softmax_cross_entropy_with_logits_v2 loss function

			TensorFlow documentation: 
			https://www.tensorflow.org/api_docs/python/tf/nn/softmax_cross_entropy_with_logits_v2
		'''
		loss_function = tf.nn.softmax_cross_entropy_with_logits_v2(logits=input, labels=labels)
		self.loss = loss_function
		return loss_function

	def create_optimizer(self):
		''' Default optimizer will be AdamOptmizer

			Returns:
				- Instance of AdamOptimizer with learning rate set

			TensorFlow documentation: 
			https://www.tensorflow.org/api_docs/python/tf/train/AdamOptimizer
		'''
		bsscs_optmizer = tf.train.AdamOptimizer(learning_rate=self.learning_rate)
		self.optimizer = bsscs_optmizer
		return bsscs_optmizer

	def create_classifier(self, neurons, input=None):
		''' Creates a fully connected classifier
			
			Input:
				- neurons: an array of neurons for each layer
				- input: input tensor object for the intial layer

			Returns:
				- array of tensor objects connected within the graph
		'''
		return_arr = []
		if input is None:
			input = tf.placeholder(tf.float32, shape=[None, neurons[0]])

		return_arr.append(input)
		
		for layer in neurons:
			curr_layer = self.create_layer(neurons=layer, input=input)
			input = curr_layer
			return_arr.append(curr_layer)

		self.layers = return_arr
		return return_arr