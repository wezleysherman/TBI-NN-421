# Authors: Wezley Sherman
#
# Reference Attributes: Official TensorFlow documentation from Google
# Dense layers:
# https://www.tensorflow.org/api_docs/python/tf/layers/dense
# l2 Regularizer:
# https://www.tensorflow.org/api_docs/python/tf/contrib/layers/l2_regularizer
# Tensorflow Relu activation:
# https://www.tensorflow.org/api_docs/python/tf/nn/relu

class BSSCS_CLASSIFIER:
	def __init__(self, l2_reg, learning_rate, steps, batch, loss, optimizer):
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

	def create_layer(self, input, neurons, activation=tf.nn.relu):
		# To-Do: implement
		return

	def create_loss_function(self, input, labels):
		# To-Do: implement
		# Will use Softmax Cross Entropy
		return

	def create_optimizer(self, loss_function)
		# To-Do: implement
		# Will use Adam Optmizer
		return

	def create_classifier(self, neurons, input)
		# To-Do: implement
		return