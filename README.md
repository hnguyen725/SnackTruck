This is a simple food truck ordering application that allows user to perform the following use cases:
1.	Place an order on selected menu items.
2.	Filter the menu list by veggie, non-veggie, or both.
3.	Add new menu items to the menu list.


Currently, the app does not distinguish between a custom or an operator. Below is one of the approaches to solve this problem:
-	By default, the app will not display an ‘Add’ button.
-	We can add a ‘Log in as operator’ option on the menu of the top action bar.
-	When an operator selects to log in, a new page or dialog will display basic username, password, sign in button, and a cancel button.
-	A back-end server will verify if the credential is correct.
-	If the log in is successful, the top action bar will display 2 options:
    - Add for adding a menu item.
    - Log out to log the operator out of the session and allow other customers to use the app.
-	Pressing on the ‘Add’ option will allow the operator to add a new menu item like current functionality.


Implementation and libraries:
- ViewModel – the current implementation is using a main FoodMenuViewModel separate data logic from the UI (Fragments). The benefit of using a view model is that it will help store data through UI rotation and allow data to be shared between different fragments hosted on the same activity. Another big benefit is when view model data is changed, it will notify the observers of anything changes and update the UI accordingly.
- Espresso – The current project is using Espresso to automate UI testing. Current MainActivityTest class is covering 3 main use cases from above.
