# projet Réseaux Daveine Capucine et Roussel Alan

                                                                                                                 
                            ███╗                                                                                 
                          ███╔═╝                                                                                 
                          ╚══╝                                                                                   
██████╗  ██████╗ ██╗  ██╗███████╗    ██╗   ██╗███╗   ██╗██╗██╗   ██╗███████╗██████╗ ███████╗██╗████████╗██╗   ██╗
██╔══██╗██╔═══██╗██║ ██╔╝██╔════╝    ██║   ██║████╗  ██║██║██║   ██║██╔════╝██╔══██╗██╔════╝██║╚══██╔══╝╚██╗ ██╔╝
██████╔╝██║   ██║█████╔╝ █████╗█████╗██║   ██║██╔██╗ ██║██║██║   ██║█████╗  ██████╔╝███████╗██║   ██║    ╚████╔╝ 
██╔═══╝ ██║   ██║██╔═██╗ ██╔══╝╚════╝██║   ██║██║╚██╗██║██║╚██╗ ██╔╝██╔══╝  ██╔══██╗╚════██║██║   ██║     ╚██╔╝  
██║     ╚██████╔╝██║  ██╗███████╗    ╚██████╔╝██║ ╚████║██║ ╚████╔╝ ███████╗██║  ██║███████║██║   ██║      ██║   
╚═╝      ╚═════╝ ╚═╝  ╚═╝╚══════╝     ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝╚══════╝╚═╝   ╚═╝      ╚═╝   

## Partie serveur

Le serveur est fait en Java et ce trouve dans le fichiers /java.
Nous avons utilisé gradle pour l'architecture de l'application le code source ce trouve dans: /app/src/main/java/Poke_University

Pour run le code ou build il y a un éxécutable gradlew qui permet de faire ./gradlew run ou ./gradlew build jar.
Si vous lancer un build le résultat se trouve dans app/build.

## Partie Client

Le client est fait en C et ce trouve dans /Client.
Le code source se trouve dans /src.

Un make file est à votre disposition pour créer l'éxécutable avec la commande make.
Une fois l'éxécutable générer vous le retouverer dans /build, il se nomme client.
Deux autres éxécutables l'accompagne, team et tchat, ils sont appelé par client.


