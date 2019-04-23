import os,sys
import json
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from PIL import Image
import tensorflow as tf

with tf.Session() as sess:

    input_path = sys.argv[1]
    output_path = sys.argv[2]
    img = Image.open(input_path)
    img.save(output_path)
