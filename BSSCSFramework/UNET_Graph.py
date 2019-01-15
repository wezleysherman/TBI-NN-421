# Authors: Wezley Sherman
#
# Reference Attributes: U-Net: Convolutional Networks for Biomedical Image Segmentation
# Authors: Olaf Ronneberger, Philipp Fischer, and Thomas Brox
#
# This class is a part of the BSSCS Net Framework to import DICOM images and batch them for the UNET
#
# BSSCS Docs Importer location: TBD
#
# Citation:
# Ronneberger, et al. “U-Net: Convolutional Networks for Biomedical Image Segmentation.” 
#     [Astro-Ph/0005112] A Determination of the Hubble Constant from Cepheid Distances and a Model of the Local Peculiar Velocity Field, 
#     American Physical Society, 18 May 2015, arxiv.org/abs/1505.04597.
# Site:
# https://arxiv.org/pdf/1505.04597.pdf
import tensorflow as tf
from UNET_Data import UNET_DATA

class BSSCS_UNET:
	def __init__(self, iterations, batch_size, data_class, learning_rate=0.001):
		self.learning_rate = learning_rate
		self.iterations = iterations
		self.batch_size = batch_size
		self.data_class = data_class

	def generate_unet_arch(self, input):
		''' Handles generating a TF Implementation of a UNET utilizing the architecture discussed in
		    "U-Net: Convolutional Networks for Biomedical Image Segmentation" by Ronneberger, Fischer, and Brox

		    The architecture for the UNET is not ours and all accrediation goes to Olaf Ronneberger, Philipp Fischer, and Thomas Brox. 
		    We are not claiming any ownership for the architecture. 
		    Implementing the UNET arch is comparable to implementing selection sort.

		    tf.layers.conv2d docs: https://www.tensorflow.org/api_docs/python/tf/layers/conv2d
			tf.layers.conv2d_transpost for up convolutions: https://www.tensorflow.org/api_docs/python/tf/layers/conv2d_transpose
			tf.concat for the copy and crop methods: https://www.tensorflow.org/api_docs/python/tf/concat
			tf.slice for cropping the tensors: https://www.tensorflow.org/api_docs/python/tf/slice
			Hopefully I implemented this correctly  ¯\_(ツ)_/¯

			Quick guide on cropping a tensor -..

			So after some research through the doc's I found out that we can't just crop it as if it were an image, because we are dealing with Tensors (matricies of data).

			In order to crop a tensor we must use TensorFlow's slice function (https://www.tensorflow.org/api_docs/python/tf/slice)
			
			Here we are cropping the convolutional layer we are upsampling to be the size of the convolutional layer we are concating to. 
			I'm starting at the base coordinates for the tensor object, and am cropping JUST the images (or filters). Thus why we have [-1, size_x, size_y, -1]. 
			The '-1' values are there to ensure we are keeping the remaining elements of the dimension (AKA our batch size and number of filters) . 
			From TF Docs on the -1 values: " If size[i] is -1, all remaining elements in dimension i are included in the slice. In other words, this is equivalent to setting: size[i] = input.dim_size(i) - begin[i]"

			Once the tensor is properly cropped (Where each filter is the same size as the tensor we are copying into), we can concat the tensors. 
			This allows us to copy in all of the previous filters into the current tensor. The final shape will be: [Batch, Img_X, Img_Y, [Filters_A + Filters_B]]
		'''
		# first block in UNET --> Concat with the final block
		convolution_layer_1 = tf.layers.conv2d(inputs=input, filters=64, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_2 = tf.layers.conv2d(inputs=convolution_layer_1, filters=64, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		max_pooling_layer_1 = tf.layers.max_pooling2d(inputs=convolution_layer_2, pool_size=[2, 2], strides=1, padding="VALID")

		# second block in UNET --> Concat with second to final block
		convolution_layer_3 = tf.layers.conv2d(inputs=max_pooling_layer_1, filters=128, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_4 = tf.layers.conv2d(inputs=convolution_layer_3, filters=128, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		max_pooling_layer_2 = tf.layers.max_pooling2d(inputs=convolution_layer_4, pool_size=[2, 2], strides=1, padding="VALID")

		# third block in UNET --> Concat with third from final block
		convolution_layer_5 = tf.layers.conv2d(inputs=max_pooling_layer_2, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_6 = tf.layers.conv2d(inputs=convolution_layer_5, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		max_pooling_layer_3 = tf.layers.max_pooling2d(inputs=convolution_layer_6, pool_size=[2, 2], strides=1, padding="VALID")

		# fourth block in UNET --> Concat with fourth from final block
		convolution_layer_7 = tf.layers.conv2d(inputs=max_pooling_layer_3, filters=512, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_8 = tf.layers.conv2d(inputs=convolution_layer_7, filters=512, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		max_pooling_layer_4 = tf.layers.max_pooling2d(inputs=convolution_layer_8, pool_size=[2, 2], strides=1, padding="VALID")

		# middle UNET block
		convolution_layer_9 = tf.layers.conv2d(inputs=max_pooling_layer_4, filters=1024, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_10 = tf.layers.conv2d(inputs=convolution_layer_9, filters=1024, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_1 = tf.layers.conv2d_transpose(inputs=convolution_layer_10, filters=1024, kernel_size=[2, 2], strides=1, padding="SAME")
		
		# fourth from last 
		convolution_up_1 = tf.slice(convolution_up_1, [0, 0, 0, 0], [-1, convolution_layer_8.shape[1], convolution_layer_8.shape[2], -1])
		concat_layer_1 = tf.concat([convolution_up_1, convolution_layer_8], axis=3) # Note: Experiment with the axis to ensure it is correct. Are we copying the batches or the filters? -- However; different axis's cause an error.
		# print(concat_layer_1.shape) # Comes out to be [Batch_Size, Image_X, Image_Y, (Filters_Conv_8 + Filters_Conv_Up_1)]
		convolution_layer_11 = tf.layers.conv2d(inputs=concat_layer_1, filters=512, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_12 = tf.layers.conv2d(inputs=convolution_layer_11, filters=512, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_2 = tf.layers.conv2d_transpose(inputs=convolution_layer_12, filters=512, kernel_size=[2, 2], strides=1, padding="SAME")

		
		# third from last
		convolution_up_2 = tf.slice(convolution_up_2, [0, 0, 0, 0], [-1, convolution_layer_6.shape[1], convolution_layer_6.shape[2], -1])
		concat_layer_1 = tf.concat([convolution_up_2, convolution_layer_6], axis=3)
		convolution_layer_13 = tf.layers.conv2d(inputs=convolution_up_2, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_14 = tf.layers.conv2d(inputs=convolution_layer_13, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_3 = tf.layers.conv2d_transpose(inputs=convolution_layer_14, filters=256, kernel_size=[2, 2], strides=1, padding="SAME")

		# second from last
		convolution_up_3 = tf.slice(convolution_up_3, [0, 0, 0, 0], [-1, convolution_layer_4.shape[1], convolution_layer_4.shape[2], -1])
		concat_layer_1 = tf.concat([convolution_up_3, convolution_layer_4], axis=3)
		convolution_layer_15 = tf.layers.conv2d(inputs=convolution_up_3, filters=256, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_16 = tf.layers.conv2d(inputs=convolution_layer_15, filters=128, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_4 = tf.layers.conv2d_transpose(inputs=convolution_layer_16, filters=128, kernel_size=[2, 2], strides=1, padding="SAME")

		# last block
		convolution_up_4 = tf.slice(convolution_up_4, [0, 0, 0, 0], [-1, convolution_layer_2.shape[1], convolution_layer_2.shape[2], -1])
		concat_layer_1 = tf.concat([convolution_up_4, convolution_layer_2], axis=3)
		convolution_layer_17 = tf.layers.conv2d(inputs=convolution_up_4, filters=128, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_18 = tf.layers.conv2d(inputs=convolution_layer_17, filters=64, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_layer_19 = tf.layers.conv2d(inputs=convolution_layer_18, filters=64, kernel_size=[3, 3], strides=1, padding="SAME", activation=tf.nn.relu)
		convolution_up_5 = tf.layers.conv2d_transpose(inputs=convolution_layer_19, filters=2, kernel_size=[1, 1], strides=1, padding="SAME")
		print(convolution_up_5.shape)
		return convolution_up_5

	def train_unet(self):
		''' Handles training a UNET based off the data fed to it
		'''

		# This is where I would put my loss and optimization functions -..
		# ..
		# ..
		# IF I HAD ONE!
		#
		# Meme Reference: https://www.youtube.com/watch?v=ciWPFvLS5IY
		#
		# On a serious note -.. Here is where we will plug in the deep regressor once that's built.
		# After a UNET run the image will be passed to the deep regressor.
		# The regressor will contain the loss function we are optimizing to.
		input_ph = tf.placeholder(tf.float32, shape=[None, 572, 572, 1]) # Placeholder vals were given by paper in initial layer -- these numbers were referenced from the paper.
		conv_input = self.generate_unet_arch(input_ph)
		with tf.Session() as session:
			for iteration in range(0, self.iterations): # counts for epochs -- or how many times we go through our data
				for batch in range(0, self.batch_size):
					y_b, X_b = self.data_class.get_next_batch()
					session.run(conv_input, feed_dict={input:X_b})

				if iteration % 500 == 0:
					# Evaluate mse loss here and print the value
					print("Passed 500 iterations with mse: ")
		

	def test_unet(self, graph_out, input_x):
		''' Runs a trained UNET through an evaluation/test phase to detect errors

			Parameters:
				- graph_out: conv2d_tranpose tensor - The last layer in the graph
				- input_x: image_arr  - Image array of shape [None, x, y, 1]

			Returns:
				- output: Returns the output of the unet graph
		'''
		with tf.Session() as session:
			output = session.run(graph_out, feed_dict={input:input_x})

		return output

	def save_graph(self):
		''' Saves a UNET graph
		'''
		# To-Do: Implement when deep regressor is finished
		return None

	def load_graph(self):
		''' Loads a UNET graph
		'''
		# To-Do: Implement when deep regressor is finished
		return None

bsscs_data = UNET_DATA([1, 2, 3], [1, 2, 3])
# quick test for errors
bsscs = BSSCS_UNET(1, 1, bsscs_data)
# Oh boy, this is scary
bsscs.train_unet()