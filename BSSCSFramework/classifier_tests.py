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

class ClassifierNetworkTests(unittest.TestCase);