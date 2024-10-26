scrabblefile = open("enable.txt")
scrabble = set()
scrabblelist = list()
while True:
    line = scrabblefile.readline()
    if not line:
        break
    word = line.upper().rstrip()
    scrabble.add(word)
    scrabblelist.append(word)

scrabblefile.close()

dictionary = open("unigram-freq.csv")
unigrams = list()
unigramsset = set()
while True:
    line = dictionary.readline()
    word = line.split(',')[0].upper().rstrip()
    if not line:
        break
    unigrams.append(word)
    unigramsset.add(word)
dictionary.close()

bad = open("bad-words.txt")
badwords = set()
while True:
    line = bad.readline()
    if not line:
        break
    badwords.add(line.upper().rstrip())
bad.close()

print("Scrabble length = %s" % len(scrabble))
print("Unigram length = %s" % len(unigrams))

l0split = 25000
l1split = 100000

level0 = set()
for i in range(0, l0split):
    word = unigrams[i]
    if word in scrabble and word not in badwords:
        level0.add(word)

level1 = set()
for i in range(l0split, l1split):
    word = unigrams[i]
    if word in scrabble and word not in badwords:
        level1.add(word)

level2 = set()
for i in range(l1split, len(unigrams)): 
    word = unigrams[i]
    if word in scrabble and word not in badwords:
        level2.add(word)

level3 = set()
for word in scrabble:
    if word not in unigramsset and word not in badwords:
        level3.add(word)

level4 = set()
for word in badwords:
    if word in scrabble:
        level4.add(word)

print("Level0 length = %s" % len(level0))
print("Level1 length = %s" % len(level1))
print("Level2 length = %s" % len(level2))
print("Level3 length = %s" % len(level3))
print("Level4 length = %s" % len(level4))

file = open("../../app/src/main/assets/dictionary.txt", 'w')
for word in scrabblelist:
    if word in level0:
        l = 0
    elif word in level1:
        l = 1
    elif word in level2:
        l = 2
    elif word in level3:
        l = 3
    elif word in level4:
        l = 4
    file.write("%s,%s\n" % (word, l))

file.close()

