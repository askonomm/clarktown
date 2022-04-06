(ns clarktown.parsers.list-block
  (:require
    [clojure.string :as string]))


(defn is?
  "Determines whether we're dealing with a list block or not."
  [block]
  (->> (string/trim block)
       (re-matches #"(?s)^(\d\.\s|\*{1}\s).*$")))


(defn string->indent-n
  "Returns the indentation count from left of `str`, which must be
  in spaces and not tabs."
  [str]
  (count (take-while #{\space} str)))


(defn compose-items-with-indent-guides
  "Composes a vector of maps from given `block` that adds a unique
  ID to each line as well as its `indent-n` which is used later
  on to determine hierarchies. "
  [block]
  (->> (string/split-lines block)
       (mapv
         (fn [line]
           {:id (random-uuid)
            :indent-n (string->indent-n line)
            :value (-> line
                       string/trim)}))))


(defn find-parent-id
  "Assuming a 1-level `items`, will attempt to find the parent `id`
  of the item at given `index`. Will return `nil` otherwise."
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
  "Composes a 1-level list of items from `block` and adds parent
  information to each if they belong to another item. The result
  of this is used to build the final data tree."
  [block]
  (let [items (compose-items-with-indent-guides block)]
    (->> items
         (map-indexed
           (fn [index line]
             (merge line {:parent (find-parent-id items index)}))))))


(defn add-to-parent
  "Recursively scans `items`, which can be multiple levels deep,
  and tries to find a home for `item` according to its parent ID."
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
  "Given a `block`, composes a data representation of it based on
  the indentation of each line."
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
  "Renders an ordered/un-ordered list hierarchy from given `items`."
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
  "Renders the list block"
  [block _]
  (-> (compose-item-tree block)
      (render-items)))