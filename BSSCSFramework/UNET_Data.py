# Authors: Wezley Sherman
#
# Reference Attributes: Pydicom documentation, glob documentation
# https://pydicom.github.io/pydicom/stable/getting_started.html
# https://pymotw.com/2/glob/
#
# This class is a part of the BSSCS Net Framework to import DICOM images
#
# BSSCS Docs Importer location: BSSCS_DOCS/dicom.html
from DICOMImporter import DICOMImporter
import pandas as pd
import numpy as np
from PIL import Image
from tqdm import tqdm
import math

class UNET_DATA:
	def __init__(self, batch_size=0, labels_arr=None, image_arr=None, csv_path=None, images_path=None):
		self.current_batch = 0
		self.batch_size = 0
		self.total_batches = 0
		self.labels = labels_arr
		self.images = image_arr

		if csv_path and images_path:
			print("Found CSV")
			data_frame = self.open_csv(csv_path)
			self.data_dictionary = self.fetch_images_with_csv(images_path, data_frame)
			self.data_keys = list(self.data_dictionary.keys())
		else:
			print("No CSV")
			self.data_dictionary = None

	def set_batch_size(self, new_size):
		''' Responsible for setting a new batch size

			Input:
				- new_size: int -- corresponds to the new batch size we want to assign
		'''
		self.batch_size = new_size

	def get_batch_size(self):
		''' Responsible for returning the batch size for the class

			Returns:
				-int -- corresponds to the batch size
		'''
		return self.batch_size

	def get_total_batches(self):
		''' Responsible for returning how many batches of data are in our dataset

			Returns:
				- int -- corresponds to the number of batches in our dataset
		'''
		return math.floor(self.images/batch_size)

	
	def get_next_batch(self):
		'''	Responsible for batching the data arrays and returning them
		
			Returns: 
				label_batch: arr -- batch of labels for the associated image
				image_batch: arr -- batch of images for the associated labels
		'''
		start_pos = (self.batch_size * self.current_batch)
		end_pos =  (self.batch_size * self.current_batch+1)

		label_batch = []
		image_batch = []
		if not self.data_dictionary:
			label_batch = self.labels[start_pos:end_pos]
			image_batch = self.images[start_pos:end_pos]
		else:
			label_batch_keys = self.data_keys[start_pos:end_pos]
			for key in label_batch_keys:
				image_batch.append(np.resize(np.array(self.data_dictionary[key]['image_arr']), (512, 512, 1)))
				label_batch.append(self.data_dictionary[key]['labels'])

		# Reset the current batch once we've iterated through all of our data
		self.current_batch += 1
		if(self.current_batch >= self.total_batches):
			self.current_batch = 0

		return label_batch, image_batch
		
	def fetch_data(self, path_to_csv):
		''' Handles fetching the data from the DICOM Importer
		
			Assigns:
				self.labels
				self.images
		'''
		images_arr, labels_arr = self.import_labels_from_csv(path_to_csv)
		self.images = images_arr
		self.labels = labels_arr
		
	def import_labels_from_csv(self, path):
		''' Handles opening a CSV of data and reading in the information to match
			The image with the label.
			
			Input: 
				- path: String -- path to the CSV
			
			Returns:
				- images: list -- list of image file names
				- labels: list -- list of boolean labels
		'''
		csv_dataframe = pd.read_csv(path)
		images = list(csv_dataframe['file_name'])
		labels = list(csv_dataframe['has_tbi'])
		return images, labels

	def open_csv(self, path):
		''' Handles opening a labels CSV for the test set and returning the datframe

			Input:
				- path: String -- path to CSV

			Returns:
				- csv_dataframe: pandas dataframe for labels

		'''
		csv_dataframe = pd.read_csv(path)
		return csv_dataframe

	def fetch_images_with_csv(self, path, dataframe):
		''' Handles fetching images from a filepath and constructs a dictionary with their labels

			Input:
				- path: String -- path to data folder
				- dataframe: pandas dataframe

			Returns:
				- Dictionary of data structured as:
				{
					image_name : {
						image_arr: [2D pixel array],
						labels: [labels array]
					}
				}
		'''
		data_dictionary = {}
		count = 0
		for row in tqdm(dataframe.iterrows()):
			data_dictionary[row[1][0]] = {}
			image_path = path +'/' + row[1][0] + '_blue.png'
			image = list(Image.open(image_path).getdata())
			data_dictionary[row[1][0]]['image_arr'] = image
			data_dictionary[row[1][0]]['labels'] = np.resize(np.array(row[1][1].split(' ')[0]), (1))
			if count == 1:
				break
			count += 1
		return data_dictionary

