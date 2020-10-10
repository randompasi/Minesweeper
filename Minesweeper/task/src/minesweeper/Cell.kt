package minesweeper

class Cell {
     var realValue = '/'
        get() = field
        set(value) {
            field = value
        }
     var fakeValue = '.'
      get() = field
        set(value) {
            field = value
        }
    var status = State.HIDDEN
        get() = field
        set(value) {
            field = value
        }


}