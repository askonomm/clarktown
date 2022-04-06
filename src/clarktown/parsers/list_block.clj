(ns clarktown.parsers.list-block
  (:require
    [clojure.string :as string]))


(defn is?
  "Determines whether we're dealing with a list block or not."
  [block]
  (->> (string/trim block)
       (re-matches #"(?s)^(\d\.|\*).*$")))


(defn string->indent-n
  [str]
  (count (take-while #{\space} str)))


(defn compose-items-with-indent-guides
  [block]
  (->> (string/split-lines block)
       (mapv
         (fn [line]
           {:id (random-uuid)
            :indent-n (string->indent-n line)
            :value (-> line
                       string/trim)}))))


(defn find-parent-id
  [items index]
  (let [indent-n-at-index (:indent-n (nth items index))]
    (-> (->> (split-at index items)
            first
            reverse
            (remove #(or (> (:indent-n %) indent-n-at-index)
                         (= (:indent-n %) indent-n-at-index)))
            first
            :id))))


(defn compose-items-with-parents
  [block]
  (let [items (compose-items-with-indent-guides block)]
    (->> items
         (map-indexed
           (fn [index line]
             (merge line {:parent (find-parent-id items index)}))))))


(defn add-to-parent
  [items item]
  (->> items
       (mapv
         (fn [i]
           (if (= (:id i) (:parent item))
             (if (:items i)
               (assoc i :items (concat (:items i) [item]))
               (assoc i :items [item]))
             (if (:items i)
               (assoc i :items (add-to-parent (:items i) item))
               i))))))


(defn compose-item-tree
  [block]
  (loop [result []
         items (compose-items-with-parents block)]
    (if (empty? items)
      result
      (let [item (first items)
            parent (:parent item)
            new-item {:id (:id item)
                      :value (:value item)}]
        (recur (if parent
                 (add-to-parent result item)
                 (concat result [new-item]))
               (drop 1 items))))))


(defn render-items
  [items]
  (loop [result ""
         inner-items items]
    (if (empty? inner-items)
      (if (string/starts-with? (:value (first items)) "*")
        (str "<ul>" result "</ul>")
        (str "<ol>" result "</ol>"))
      (let [inner-item (first inner-items)
            value (if (string/starts-with? (:value inner-item) "*")
                    (-> (string/replace-first (:value inner-item) "*" "")
                        string/trim)
                    (-> (string/replace-first (:value inner-item) #"\d\." "")
                        string/trim))]
        (recur (if (:items inner-item)
                 (str result "<li>" value (render-items (:items inner-item)) "</li>")
                 (str result "<li>" value "</li>"))
               (drop 1 inner-items))))))


(defn render
  "Renders the ordered list block"
  [block _]
  (-> (compose-item-tree block)
      (render-items)))