class Smartphone:
    def __init__(self, brand="", price=-1, storage=-1):
        self.Brand = brand
        self.Price = price
        self.Storage = storage
    def __repr__(self):
        return f"({self.Brand}, {self.Price}, {self.Storage})"
