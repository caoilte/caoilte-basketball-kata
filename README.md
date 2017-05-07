## Results

The RealDataTests check what I assume to be the required output. 

## Assumptions

Ordinarily I would consult with the customer before making any of these assumptions.

- The two sample files are sufficient data sets for demonstrating every possible type of inconsistent and/or inaccurate event. 
- It is not desirable for downstream systems to see two events with the same 'elapsed match time'. Where two events have the same 'elapsed match time' it is safe for the first one received to be used in preference over any that come after.
- A consistent data set can have events with demonstrable gaps between them. Sample 1 does not have such gap so you could assume that such a gap is inconsistent. However,
  - This is not explicitly stated.
  - The brief also says that "we need to send data downstream as soon as possible after we have received it". 
  
  Consequently I have assumed that clients would prefer to have the latest data even if there are gaps. 
- The second sample data stream appears to contain an event outside of the first 10 minutes of the match. Since there is no in-band mechanism for determining that an event has been sent from the future, I decided to leave it in pending further discussion.

## Feedback

- This was a fun test. I particularly enjoyed playing with the bitwise operators. 
- The PrettyPrinter wasn't necessary, but it made it easier to understand the data set inconsistencies.
- It took a little bit longer than I was expecting (about four hours) and so possibly I misinterpreted the instructions.