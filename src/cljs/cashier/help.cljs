(ns cashier.help)

(def help-text
  "Trying to approximate how long a line at the supermarket will take using Fibonacci numbers.

## Theory

The older a person is, the longer it takes him or her to proceed in a line.
Same goes for the cashier.
The app ranks lines based on that crude theory:

|-----------|---------------------------|
| Age range | Number equivalent         |
|-----------|---------------------------|
| Children  | 0 (Are currently ignored) |
| Teens     | 1                         |
| Twens     | 2                         |
| Thirties  | 3                         |
| Fourties  | 5                         |
| Fifties   | 8                         |
| 50+       | 13                        |
|-----------|---------------------------|


So the smaller the number of a line the better.
"
  [:div.help.content
   [:h1 "The situation"]
   [:p "You approach check-out at your local super market and wonder: "
    "... which line to pick ...?!"]
   [:h2 "Rationale"]
   [:p
    "Different people standing in frot of you take a different time when it's their turn. "
    "The theory is, the older the longer it takes a person to pass through the line. "
    "This app tries to detect which line to pick by judging the age of the customer and the cashier. " "Every age gets assigned a number, from 1 to 13:"
    [:pre
     "|-----------|---------------------------|
| Age range | Number equivalent         |
|-----------|---------------------------|
| Children  | 0 (Are currently ignored) |
| Teens     | 1                         |
| Twens     | 2                         |
| Thirties  | 3                         |
| Fourties  | 5                         |
| Fifties   | 8                         |
| 50+       | 13                        |
|-----------|---------------------------|
"]]
   [:h2 "Usage"]
   [:h4 "Recording a line"]
   [:p "Click on add line for each line at the checkout. "
    "Select the cashiers number equivalent and all customers/carts in the line. "
    "Then click done if it's been the last line, or New when you want to record the current line and add an additional line."]
   [:h4 "Pick a line"]
   [:p "When done recording the last line, go back to the main view via `Done`. "
    "The application will show a list of all recorded lines, ordered by their ranks. "
    "The line with the lowest number is likely to be the fastest, take this one!"]

   [:h2 "Questions"]
   [:ul
    [:li "Is this serious? " [:em "No it's not, just had an hour to spare."]]
    [:li "You know that, at the time finishing inserting all the lines I could already be through them? " [:em "Sure, see question #1"]]]])
