import matplotlib.pyplot as plt 
from mpl_toolkits.mplot3d import Axes3D

file = open('res.txt', 'r')

#
# On récupère les données sur les joueurs
#

prop_denom = int(file.readline())
nb_parties = int(file.readline())

players = []
players_prop = []
end_string = file.readline()
while end_string != "START\n":
    data = end_string.split("#")
    players.append(data[0])
    players_prop.append(int(data[1])/prop_denom)
    end_string = file.readline()

#
#On récupère les résultats
#

# opti = []
# k = []
# prop_win = []

# for current_line in file.readlines():
#     data = current_line.split("#")
#     if len(data) == 2:
#         opti.append(data[0])
#         k.append(data[1])
#         prop_win.append(0)
#     else:
#         data = data[0].split("\n")
#         if (str(data[0]) == players[0]):
#             prop_win[-1] += 1

# for i in range(len(prop_win)):
#     prop_win[i] /= 100


opti_to_curve = dict()
opti = []
current_opti = -1
for current_line in file.readlines():
    data = current_line.split("#")
    if len(data) == 2:
        data[0] = int(data[0])
        if data[0] != current_opti:
            current_opti = data[0]
            opti.append(data[0])
            opti_to_curve[data[0]] = [[], []]
        opti_to_curve[data[0]][0].append(int(data[1]))
        opti_to_curve[data[0]][1].append(0)
    else:
        data = data[0].split("\n")
        if (str(data[0]) == players[0]):
            opti_to_curve[current_opti][1][-1] += 1

for key in opti:
    for i in range(len(opti_to_curve[key][1])):
        opti_to_curve[key][1][i] /= nb_parties


#
# On trace les résultats
#



# fig = plt.figure()
# ax = fig.add_subplot(111, projection='3d')
# ax.scatter(opti,k,prop_win)
print(opti)
k = 1
p = 3
# for i in opti:
#     plt.subplot(int("33"+str(k)))
#     plt.plot(opti_to_curve[i][0], opti_to_curve[i][1])
#     plt.title("Valeur optimale : "+str(i))
#     if p%3 == 0:
#         p = 2
#         k += 1
#     p += 1
#     if k >= 10:
#         break
#     #plt.show()

plt.plot(opti_to_curve[opti[0]][0], opti_to_curve[opti[0]][1])
plt.title("taux de victoire de la stratégie Justicier(0.8) contre la stratégie Saboteur(k)")
plt.xlabel("k")
plt.plot([3,13], [0.5, 0.5])

plt.show()


