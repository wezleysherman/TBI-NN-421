# Authors: Wezley Sherman
# 
# Reference Attributes: Official Python documentation
#
# Python Enum documentation:
# https://docs.python.org/3/library/enum.html

from enum import Enum

class Loss_Functions(Enum):
	SIGMOID = 1
	SOFTMAX = 1

class Optimizers(Enum):
	ADAM = 1
	GRADIENT_DESCENT = 2
	