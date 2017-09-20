#lang racket

(define emit-output-port '())

(define (set-emit-output-port port)
  (set! emit-output-port port))

(define (get-emit-output-port)
  emit-output-port)

(define (set-emit-output-file filepath)
  (set! emit-output-port (open-output-file filepath
                                           #:mode 'text
                                           #:exists 'replace)))

(define (emit-label label)
  (fprintf emit-output-port "~a: " label))

(define 