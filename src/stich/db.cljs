(ns stich.db)

(def default-db {:game-state :initial
                 :game {:round 0
                        :round-labels ["1" "2" "3" "3" "2" "1"]
                        :rounds [{:announced [1 0 1]
                                  :actual    [0 0 1]}
                                 {:announced [2 0 1]
                                  :actual    [0 2 0]}]}
                 :game-settings {;;TODO brauch ich die num-players?
                                 :num-players 3
                                 :max-cards 5
                                 :player-names []
                                 :player-names-tmp ["Alice" "Bob" "Charly"]}})