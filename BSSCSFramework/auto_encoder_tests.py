''' Authors: Wezley Sherman
	Unit Tests for the autoencoder framework implementation
	Unit Test Reference: https://docs.python.org/2/library/unittest.html
	l2 Regularizer Reference: https://www.tensorflow.org/api_docs/python/tf/contrib/layers/l2_regularizer
	Tensorflow Relu activation:
	https://www.tensorflow.org/api_docs/python/tf/nn/relu
'''
from BSSCS_AUTO_ENCODER import BSSCS_AUTO_ENCODER
import tensorflow as tf
import unittest



class AutoEncoderTests(unittest.TestCase):
	def test_auto_layer(self):
		''' Handles testing creation of a single autoencoder layer

			Preconditions: No autoencoder layer exists
			Postconditions: A single autoencoder layer has been created

			Note on regularizer: We use l2 regularization here due to efficiency on a larger dataset.
			Regularization is needed to prevent overfitting with the autoencoder.
		'''
		bsscs = BSSCS_AUTO_ENCODER(
			l2_reg=tf.contrib.layers.l2_regularizer(scale=0.001),
			learning_rate=0.001,
			steps=250, # Arbitrary
			batch_size=250, # Arbitrary
			activation=tf.nn.relu,
		)

		layer = bsscs.create_layer(neurons=512)
		self.assertTrue(layer != None)

	def test_full_auto(self):
		''' Handles testing creation of a full autoencoder

			Preconditions: No autoencoder exists
			Postconditions: A single autoencoder has been created

			Note on regularizer: We use l2 regularization here due to efficiency on a larger dataset.
			Regularization is needed to prevent overfitting with the autoencoder.
		'''
		bsscs = BSSCS_AUTO_ENCODER(
			l2_reg=tf.contrib.layers.l2_regularizer(scale=0.001),
			learning_rate=0.001,
			steps=250, # Arbitrary
			batch_size=250, # Arbitrary
			activation=tf.nn.relu,
		)

		full_network = bsscs.create_autoencoder(neurons=[512, 256, 128, 256, 512])
		self.assertTrue(len(full_network) == 6)
		self.assertTrue(full_network != None)

	def test_partial_get(self):
		''' Handles retreiving the middle layer in the autoencoder
			Preconditions: No autoencoder exists
			Postconditions: A single autoencoder has been created

			Note on regularizer: We use l2 regularization here due to efficiency on a larger dataset.
			Regularization is needed to prevent overfitting with the autoencoder.
		'''
		bsscs = BSSCS_AUTO_ENCODER(
			l2_reg=tf.contrib.layers.l2_regularizer(scale=0.001),
			learning_rate=0.001,
			steps=250, # Arbitrary
			batch_size=250, # Arbitrary
			activation=tf.nn.relu,
		)

		full_network = bsscs.create_autoencoder(neurons=[512, 256, 128, 256, 512])
		middle_layer = bsscs.get_partial()
		self.assertTrue(middle_layer == full_network[3])

if __name__ == '__main__':
	unittest.main()