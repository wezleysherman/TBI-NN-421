import os, sys
import numpy as np
from PIL import Image
import tensorflow as tf


input_path = sys.argv[1]
output_path = sys.argv[2]

checkpoint = tf.train.latest_checkpoint("")
input_image = np.array(list((Image.open(input_path)).resize((256, 256)).getdata()), dtype=np.uint8)[:,0].reshape(1, 256, 256, 1)

tf.reset_default_graph()

with tf.Session() as sess:
    saver = tf.train.import_meta_graph(checkpoint + '.meta')
    saver.restore(sess, 'unet')
    inputs = tf.get_default_graph().get_tensor_by_name('Placeholder:0')
    predictions = tf.get_default_graph().get_tensor_by_name('conv2d_transpose_4/BiasAdd:0')
    lungs = sess.run(predictions, feed_dict={inputs: input_image})
    img = Image.fromarray((np.array(lungs[0])*255).reshape(252, 252)).convert("RGB")
    img.save(output_path)
