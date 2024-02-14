# S3-FIFO: simple, scalable FIFObased algorithm with three static queues (S3-FIFO)
This implementation is based on this paper: https://dl.acm.org/doi/pdf/10.1145/3600006.3613147

##  Benefits as author claimed: 
* S3- FIFO has lower miss ratios than state-of-the-art algorithms
  across traces. 
* Moreover, S3-FIFO’s efficiency is robust — it
  has the lowest mean miss ratio on 10 of the 14 datasets. 
* FIFO queues enable S3-FIFO to achieve good scalability with 6×
  higher throughput compared to optimized LRU at 16 threads

## Warning and future work: 
* This is an initial java implementation, need to run all the test cases
* I have implemented as key value pair instead of author's suggested keeping object itself
* Needs to make it multi threaded