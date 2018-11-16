''' Authors: Wezley Sherman
	Unit Tests for the CNN framework implementation
	Unit Test Reference: https://docs.python.org/2/library/unittest.html
'''

from DICOMImporter import DICOMImporter
from BSSCS_CLASSIFIER import BSSCS_CLASSIFIER
from BSSCS_AUTO_ENCODER import BSSCS_AUTO_ENCODER

import numpy as np
import unittest
import tensorflow as tf

class ClassifierNetworkTests(unittest.TestCase):
	def test_layer_creation(self):
		''' Tests the creation of a default layer

			preconditions: No layer has been created
			postconditions: layer with default placeholder has been created
		'''
		bsscs = BSSCS_CLASSIFIER(
			l2_reg=tf.contrib.layers.l2_regularizer(scale=0.001),
			learning_rate=0.001,
			steps=250, # Arbitrary
			batch=250, # Arbitrary
		)
		self.assertTrue(bsscs != None)
		# Tests that a single layer is created and returned
		single_layer = bsscs.create_layer(512, activation=tf.nn.relu)
		self.assertTrue(single_layer != None)

	def test_loss_creation(self):
		''' Tests the creation of a loss function

			preconditions: no loss function has been created
			postconditions: A loss function has been created and exists
		'''
		bsscs = BSSCS_CLASSIFIER(
			l2_reg=tf.contrib.layers.l2_regularizer(scale=0.001),
			learning_rate=0.001,
			steps=250, # Arbitrary
			batch=250, # Arbitrary
		)
		self.assertTrue(bsscs != None)

		# Tests that a loss function is created and assigned to the bsscs object
		input_ph = tf.placeholder(tf.float32, shape=[None, 512, 512]) # 512x512 images
		labels_ph = tf.placeholder(tf.float32, shape=[None, 2]) # Testing against 2 labels
		loss_function = bsscs.create_loss_function(input=input_ph, labels=labels_ph)
		self.assertTrue(loss_function != None)

		# Test that the loss function was set in the object
		loss_function_var = bsscs.get_loss()
		self.assertTrue(loss_function_var == loss_function)

	def test_optimizer_creation(self):
		''' Tests the creation of an optimizer for the tests

			preconditions: an optmizer hasn't been created
			postconditions: an optimizer has been created
		'''
		bsscs = BSSCS_CLASSIFIER(
			l2_reg=tf.contrib.layers.l2_regularizer(scale=0.001),
			learning_rate=0.001,
			steps=250, # Arbitrary
			batch=250, # Arbitrary
		)
		self.assertTrue(bsscs != None)

		# Tests that an optimizer function is created and assigned to the bsscs object
		optimizer = bsscs.create_optimizer()
		self.assertTrue(optimizer != None)

		optimizer_var = bsscs.get_optimizer()
		self.assertTrue(optimizer == optimizer_var)


	def test_classifier_creation(self):
		''' Tests the creation of a full FC graph

			preconditions: a FC graph hasn't been created
			postconditions: a FC graph has been created covering all functions
		'''
		bsscs = BSSCS_CLASSIFIER(
			l2_reg=tf.contrib.layers.l2_regularizer(scale=0.001),
			learning_rate=0.001,
			steps=250, # Arbitrary
			batch=250, # Arbitrary
		)
		self.assertTrue(bsscs != None)

		classifier = bsscs.create_classifier([125, 12])
		self.assertTrue(classifier != None)

if __name__ == '__main__':
	unittest.main()