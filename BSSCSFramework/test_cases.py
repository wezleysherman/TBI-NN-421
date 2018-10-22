from DICOMImporter import DICOMImporter

print(DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm'))
print(DICOMImporter.open_dicom_from_folder('test_dicom'))
dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
#print(DICOMImporter.get_dicom_pixel_array(dicom_file))

dicom_files_arr = DICOMImporter.open_dicom_from_folder('test_dicom')
anon_dicoms = DICOMImporter.deidentify_dicom_images(dicom_files_arr)
print(anon_dicoms)