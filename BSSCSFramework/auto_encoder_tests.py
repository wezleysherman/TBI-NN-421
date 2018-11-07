''' Authors: Wezley Sherman
	Unit Tests for the autoencoder framework implementation
	Unit Test Reference: https://docs.python.org/2/library/unittest.html
	l2 Regularizer Reference: https://www.tensorflow.org/api_docs/python/tf/contrib/layers/l2_regularizer
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

		layer = bsscs.create_layer(512)
		self.assertTrue(layer != None)

if __name__ == '__main__':
	unittest.main()