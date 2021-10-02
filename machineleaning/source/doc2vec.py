# coding: utf-8

import MeCab
import os
from gensim.models.doc2vec import Doc2Vec
from gensim.models.doc2vec import TaggedDocument

# MeCabオブジェクトの生成
mt = MeCab.Tagger()

train_data = []
for gameFile in os.listdir('games'):
    words = []
    with open('games/%s' % gameFile, 'r', encoding='utf-8') as game:
        for line in game:
            node = mt.parseToNode(line.strip())
            while node:
                fields = node.feature.split(',')
                if fields[0] == u'名詞' or fields[0] == u'動詞' or fields[0] == u'形容詞' and node.surface is not None and node.surface != '':
                    words.append(node.surface)
                node = node.next
    train_data.append(TaggedDocument(words=words, tags=[gameFile.replace('.txt', '')]))

models = Doc2Vec(documents=train_data, min_count=1, alpha=0.005, epochs=40, sample=20, dm=1)

models.save('models/games.model')
