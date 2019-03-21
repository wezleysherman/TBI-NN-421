class Customizations():

    def __init__(self, csv_fp, img_fp, iters, batchs, layers, nodes):
        super().__init__()
        self.csv_fp = csv_fp
        self.img_fp = img_fp
        self.iters = iters
        self.batchs = batchs
        self.layers = layers
        self.nodes = nodes

    def setCSV_FP(self, csv_fp):
        self.csv_fp = csv_fp

    def getCSV_FP(self):
        return self.csv_fp

    def setIMG_FP(self, img_fp):
        self.img_fp = img_fp

    def getIMG_FP(self):
        return self.img_fp

    def setITERS(self, iters):
        self.iters = iters

    def getITERS(self):
        return self.iters

    def setBATCHS(self, batchs):
        self.batchs = batchs

    def getBATCHS(self):
        return self.batchs

    def setLAYERS(self, layers):
        self.layers = layers

    def getLAYERS(self):
        return self.layers

    def setNODES(self, nodes):
        self.nodes = nodes

    def getNODES(self):
        return self.nodes

    def toString(self):
        print("CSV Filepath: " + self.getCSV_FP())
        print("IMG Folder Filepath: " + self.getIMG_FP())
        print("Iterations: " + str(self.getITERS()))
        print("Batches: " + str(self.getBATCHS()))
        print("Layers: " + str(self.getLAYERS()))
        print("Nodes: " + str(self.getNODES()))

# nn_options = Customizations("csv", "img", 1, 2, 3, 4)
# nn_options.toString()
