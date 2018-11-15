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
		return

	def test_loss_creation(self):
		''' Tests the creation of a loss function

			preconditions: no loss function has been created
			postconditions: A loss function has been created and exists
		'''
		return

	def test_optimizer_creation(self):
		''' Tests the creation of an optimizer for the tests

			preconditions: an optmizer hasn't been created
			postconditions: an optimizer has been created
		'''
		return

	def test_classifier_creation(self):
		''' Tests the creation of a full FC graph

			preconditions: a FC graph hasn't been created
			postconditions: a FC graph has been created covering all functions
		'''
		return

if __name__ == '__main__':
	unittest.main()