;; # Walkthrough

(ns hanacloth.walkthrough
  (:require [scicloj.hanacloth.v1.api :as hana]
            [scicloj.metamorph.ml.toydata :as toydata]
            [scicloj.metamorph.ml.toydata.ggplot :as toydata.ggplot]
            [aerial.hanami.templates :as ht]
            [aerial.hanami.common :as hc]
            [tablecloth.api :as tc]
            [tablecloth.column.api :as tcc]
            [scicloj.kindly.v4.kind :as kind]
            [scicloj.kindly.v4.api :as kindly]))

:kindly/hide-code
(def md (comp kindly/hide-code kind/md))

;; ## Using the original Hanami templates & defaults

(-> (toydata/iris-ds)
    (hana/base ht/point-chart
               {:X :sepal_width
                :Y :sepal_length
                :MSIZE 200}))

;; ## Using Hanacloth templates & defaults

(-> (toydata/iris-ds)
    (hana/base hana/point-chart
               #:hana{:x :sepal_width
                      :y :sepal_length
                      :mark-size 200}))

(-> toydata.ggplot/mpg
    (hana/plot hana/boxplot-chart
               #:hana{:x :cyl
                      :y :displ}))

;; ## Adding layers

(-> (toydata/iris-ds)
    (hana/base #:hana{:x :sepal_width
                      :y :sepal_length
                      :mark-size 200})
    hana/layer-point)

(-> (toydata/iris-ds)
    (hana/layer-point #:hana{:x :sepal_width
                             :y :sepal_length
                             :mark-size 200}))

(-> (toydata/iris-ds)
    (hana/base #:hana{:x :sepal_width
                      :y :sepal_length})
    (hana/layer-point #:hana{:mark-size 200}))

(-> (toydata/iris-ds)
    (hana/base #:hana{:title "dummy"
                      :mark-color "green"
                      :x :sepal_width
                      :y :sepal_length})
    (hana/layer-line #:hana{:mark-size 4
                            :mark-color "brown"})
    (hana/layer-point #:hana{:mark-size 200}))

;; ## Updating data

(-> (toydata/iris-ds)
    (hana/base #:hana{:title "dummy"
                      :mark-color "green"
                      :x :sepal_width
                      :y :sepal_length})
    (hana/layer-line #:hana{:mark-size 4
                            :mark-color "brown"})
    (hana/update-data tc/random 5)
    (hana/layer-point #:hana{:mark-size 200}))

;; ## Processing raw vega-lite

(-> (toydata/iris-ds)
    (hana/base #:hana{:title "dummy"
                      :mark-color "green"
                      :x :sepal_width
                      :y :sepal_length})
    (hana/layer-line #:hana{:mark-size 4
                            :mark-color "brown"})
    (hana/layer-line {:MSIZE 4
                      :MCOLOR "brown"})
    (hana/update-data tc/random 20)
    (hana/layer-point #:hana{:mark-size 200})
    hana/plot
    (assoc :background "lightgrey"))

;; ## Smoothing

(-> (toydata/iris-ds)
    (hana/base #:hana{:title "dummy"
                      :mark-color "green"
                      :x :sepal_width
                      :y :sepal_length})
    hana/layer-point
    (hana/layer-smooth #:hana{:mark-color "orange"}))

(-> (toydata/iris-ds)
    (hana/base #:hana{:x :sepal_width
                      :y :sepal_length})
    hana/layer-point
    (hana/layer-smooth #:hana{:predictors [:petal_width
                                           :petal_length]}))

;; ## Grouping

(-> (toydata/iris-ds)
    (hana/base #:hana{:title "dummy"
                      :mark-color "green"
                      :color :species
                      :x :sepal_width
                      :y :sepal_length})
    hana/layer-point
    hana/layer-smooth)

(-> (toydata/iris-ds)
    (hana/base #:hana{:title "dummy"
                      :mark-color "green"
                      :color :species
                      :group []
                      :x :sepal_width
                      :y :sepal_length})
    hana/layer-point
    hana/layer-smooth)

;; ## Example: out-of-sample predictions

(-> (toydata/iris-ds)
    (tc/concat (tc/dataset {:sepal_width (range 4 10)
                            :sepal_length (repeat 6 nil)}))
    (tc/map-columns :relative-time
                    [:sepal_length]
                    #(if % "Past" "Future"))
    (hana/base #:hana{:x :sepal_width
                      :y :sepal_length
                      :color "relative-time"
                      :group []})
    hana/layer-point
    hana/layer-smooth)



;; (-> (toydata/iris-is)
;;     (hana/plot ht/rule-chart
;;                {:X :sepal-width
;;                 :Y :sepal-length
;;                 :X2 :petal-width
;;                 :Y2 :petal-length
;;                 :OPACITY 0.2
;;                 :SIZE 3
;;                 :COLOR "species"}))
