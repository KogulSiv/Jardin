import matplotlib.pyplot as plt 
from mpl_toolkits.mplot3d import Axes3D

file = open('res.txt', 'r')

nb_strats = int(file.readline())
nb_players = int(file.readline())

players_num = [0]*nb_strats
strats = []
for i in range(nb_strats):
    strats.append(file.readline().split("\n")[0])

eliminated_order = []
for i in file.readlines():
    eliminated_order.append(i.split("\n")[0])

