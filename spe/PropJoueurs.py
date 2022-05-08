import matplotlib.pyplot as plt 
from mpl_toolkits.mplot3d import Axes3D

file = open('res.txt', 'r', encoding='utf-8')

# NOMBRE DE STRATEGIES
# NOMBRE DE JOUEURS EN TOUT
# POUR CHAQUE STRATEGIE 1 LIGNE DONNANT LE NOMBRE DE JOUEURS PAR STRATEGIE (ID DE STRAT PAR ORDRE CROISSANT)
# L'ID DE LA STRATEGIE DU JOUEUR QUI A ETE ELIMINE (1 PAR LIGNE)


print(file.readline())
print(file.readline())
print(file.readline())
print(file.readline())
print(file.readline())
nb_strats = int(file.readline())
nb_players = int(file.readline())

players_num = [0]*nb_strats
#strats = []
for i in range(nb_strats):
    #strats.append(file.readline().split("\n")[0])
    players_num[int(file.readline().split("\n")[0])] += 1

eliminated_order = []
for i in file.readlines():
    eliminated_order.append(int(i.split("\n")[0]))

NbJoueursStrat = []
for i in range(nb_strats):
    NbJoueursStrat.append([players_num[i]])

for idx in eliminated_order:
    for i in range(nb_strats):
        NbJoueursStrat[i].append(NbJoueursStrat[i][-1])
        NbJoueursStrat[idx][-1] -= 1 #Si le joueur de strat éliminé, on enlève 1

T = []
for i in range(nb_players):
    T.append(i)

for i in range(nb_strats):
    plt.plot(T, NbJoueursStrat[i])
    plt.show()

