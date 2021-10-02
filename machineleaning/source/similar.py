import os
import sys
from gensim.models.doc2vec import Doc2Vec
from gensim import models 

models = models.Doc2Vec.load('models/games.model')

print(sys.argv[1])
for items in models.dv.most_similar(sys.argv[1], topn=10):
    print(str(items[0] + ' : ' + str(items[1])))
