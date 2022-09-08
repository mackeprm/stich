(ns stich.db)

(def default-db {:game-state :initial
                 :game {:round 0
                        :round-labels ["1" "2" "3" "3" "2" "1"]
                        :rounds []}
                 :game-settings {
                                 :num-players 3
                                 :max-cards 5
                                 :player-names []
                                 :player-names-tmp ["Alice" "Bob" "Charly"]}})